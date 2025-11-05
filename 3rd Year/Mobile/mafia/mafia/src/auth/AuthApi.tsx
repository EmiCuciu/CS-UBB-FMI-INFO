import axios, { AxiosResponse } from 'axios';
import { getLogger } from '../core';

const log = getLogger('AuthApi');

const baseUrl = '127.0.0.1:3000';
const authUrl = `http://${baseUrl}/api/auth`;

export interface AuthResponse {
  token: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

const config = {
  headers: { 'Content-Type': 'application/json' },
};

function withLogs<T>(promise: Promise<AxiosResponse<T>>, fnName: string): Promise<T> {
  log(`${fnName} - started`);
  return promise
    .then((res: AxiosResponse<T>) => {
      log(`${fnName} - succeeded`);
      return Promise.resolve(res.data);
    })
    .catch((err) => {
      log(`${fnName} - failed`, err);
      return Promise.reject(err);
    });
}

export const login = (credentials: LoginCredentials): Promise<AuthResponse> =>
  withLogs<AuthResponse>(
    axios.post(`${authUrl}/login`, credentials, config),
    'login'
  );

export const signup = (credentials: LoginCredentials): Promise<AuthResponse> =>
  withLogs<AuthResponse>(
    axios.post(`${authUrl}/signup`, credentials, config),
    'signup'
  );
