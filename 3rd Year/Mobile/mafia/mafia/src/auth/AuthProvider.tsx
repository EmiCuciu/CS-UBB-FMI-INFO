import React, { useCallback, useEffect, useState } from 'react';
import { getLogger } from '../core';
import { login as loginApi, signup as signupApi, LoginCredentials } from './AuthApi';

const log = getLogger('AuthProvider');

type LoginFn = (credentials: LoginCredentials) => Promise<void>;
type SignupFn = (credentials: LoginCredentials) => Promise<void>;
type LogoutFn = () => void;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  login?: LoginFn;
  signup?: SignupFn;
  logout?: LogoutFn;
  pendingAuthentication?: boolean;
  username?: string;
  password?: string;
  token: string;
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
  token: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const {
    isAuthenticated,
    isAuthenticating,
    authenticationError,
    pendingAuthentication,
    token,
  } = state;

  const login = useCallback<LoginFn>(loginCallback, []);
  const signup = useCallback<SignupFn>(signupCallback, []);
  const logout = useCallback<LogoutFn>(logoutCallback, []);

  useEffect(authenticationEffect, [pendingAuthentication]);

  const value = {
    isAuthenticated,
    login,
    signup,
    logout,
    isAuthenticating,
    authenticationError,
    token,
    username: state.username,
  };

  log('render');
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;

  function loginCallback(credentials: LoginCredentials): Promise<void> {
    log('login');
    setState({
      ...state,
      pendingAuthentication: true,
      username: credentials.username,
      password: credentials.password,
    });
    return Promise.resolve();
  }

  function signupCallback(credentials: LoginCredentials): Promise<void> {
    log('signup');
    setState({
      ...state,
      pendingAuthentication: true,
      username: credentials.username,
      password: credentials.password,
    });
    return Promise.resolve();
  }

  function logoutCallback(): void {
    log('logout');
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('mafiots');
    localStorage.removeItem('pendingOperations');
    setState({
      ...state,
      isAuthenticated: false,
      token: '',
      username: undefined,
    });
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    };

    async function authenticate() {
      const storedToken = localStorage.getItem('token');
      const storedUsername = localStorage.getItem('username');
      if (storedToken) {
        log('Found stored token');
        setState({
          ...state,
          token: storedToken,
          username: storedUsername || undefined,
          isAuthenticated: true,
          pendingAuthentication: false,
        });
        return;
      }

      if (!pendingAuthentication) {
        log('authenticate, !pendingAuthentication, return');
        return;
      }

      try {
        log('authenticate...');
        setState({
          ...state,
          isAuthenticating: true,
        });

        const { username, password } = state;
        let response;
        
        try {
          response = await loginApi({ username: username!, password: password! });
        } catch {
          log('Login failed, trying signup');
          response = await signupApi({ username: username!, password: password! });
        }

        const { token } = response;
        localStorage.setItem('token', token);
        localStorage.setItem('username', username!);

        log('authenticate succeeded');
        if (!canceled) {
          setState({
            ...state,
            token,
            username,
            pendingAuthentication: false,
            isAuthenticated: true,
            isAuthenticating: false,
          });
        }
      } catch (error) {
        if (canceled) {
          return;
        }
        log('authenticate failed');
        setState({
          ...state,
          authenticationError: error as Error,
          pendingAuthentication: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
