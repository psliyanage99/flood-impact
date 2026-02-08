import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { CheckCircle, XCircle, Loader, ArrowLeft } from 'lucide-react';
import logo from '../assets/app_logoo.png';


const API_BASE_URL = '';

const VerifyEmail = ({ onBackToLogin }) => {
  const [status, setStatus] = useState('verifying'); // verifying, success, error
  const [message, setMessage] = useState('Verifying your email address...');

  useEffect(() => {
    const verifyToken = async () => {
      // Get token from URL query parameters
      const params = new URLSearchParams(window.location.search);
      const token = params.get('token');

      if (!token) {
        setStatus('error');
        setMessage('Invalid verification link.');
        return;
      }

      try {
        await axios.get(`${API_BASE_URL}/api/auth/verify?token=${token}`);
        setStatus('success');
        setMessage('Email verified successfully! You can now log in.');
      } catch (error) {
        setStatus('error');
        setMessage(error.response?.data?.message || 'Verification failed or link expired.');
      }
    };

    verifyToken();
  }, []);

  return (
    <div className="min-h-[100dvh] w-full bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center p-4 md:p-6 overflow-y-auto">
      <div className="fixed inset-0 bg-[linear-gradient(to_right,#e0e7ff_1px,transparent_1px),linear-gradient(to_bottom,#e0e7ff_1px,transparent_1px)] bg-[size:4rem_4rem] opacity-30 pointer-events-none"></div>
      
      <div className="relative z-10 w-full max-w-md my-auto">
        <div className="bg-white/80 backdrop-blur-xl border-2 border-gray-200 rounded-3xl shadow-xl overflow-hidden transition-all duration-300">
          
          <div className="p-6 md:p-8 pb-4 text-center">
            <div className="flex justify-center mb-6">
              <div className="relative">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-300/50 to-indigo-300/50 rounded-3xl blur-xl"></div>
                <div className="relative w-20 h-20 bg-gradient-to-br from-blue-600 to-indigo-600 rounded-2xl flex items-center justify-center shadow-lg overflow-hidden">
                  <img src={logo} alt="Logo" className="w-full h-full object-cover" />
                </div>
              </div>
            </div>
            
            <h1 className="text-3xl font-black text-gray-900 mb-2 tracking-tight">
              Email Verification
            </h1>
          </div>

          <div className="px-6 md:px-8 pb-8">
            <div className={`p-6 rounded-2xl border-2 flex flex-col items-center text-center gap-4 ${
                status === 'success' ? 'bg-green-50 border-green-200' : 
                status === 'error' ? 'bg-red-50 border-red-200' : 
                'bg-blue-50 border-blue-200'
            }`}>
                
              {status === 'verifying' && <Loader className="w-12 h-12 text-blue-600 animate-spin" />}
              {status === 'success' && <CheckCircle className="w-12 h-12 text-green-600" />}
              {status === 'error' && <XCircle className="w-12 h-12 text-red-600" />}

              <div>
                <h3 className={`font-bold text-lg mb-1 ${
                    status === 'success' ? 'text-green-800' : 
                    status === 'error' ? 'text-red-800' : 
                    'text-blue-800'
                }`}>
                    {status === 'verifying' ? 'Verifying...' : status === 'success' ? 'Verified!' : 'Verification Failed'}
                </h3>
                <p className="text-sm text-gray-600 font-medium">{message}</p>
              </div>
            </div>

            <button 
                onClick={onBackToLogin} 
                className="w-full mt-6 py-4 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-xl font-bold shadow-lg hover:opacity-90 transition-all flex items-center justify-center gap-2"
            >
                <ArrowLeft className="w-5 h-5" /> Back to Login
            </button>
          </div>

        </div>
      </div>
    </div>
  );
};

export default VerifyEmail;