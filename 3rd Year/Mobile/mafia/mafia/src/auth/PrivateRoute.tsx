import React, { useContext } from 'react';
import { Redirect, Route, RouteComponentProps, RouteProps } from 'react-router-dom';
import { AuthContext } from './AuthProvider';
import { getLogger } from '../core';

const log = getLogger('PrivateRoute');

export const PrivateRoute: React.FC<RouteProps> = ({ component, ...rest }) => {
  const { isAuthenticated } = useContext(AuthContext);
  log('render', isAuthenticated);

  return (
    <Route
      {...rest}
      render={(props: RouteComponentProps) => {
        if (isAuthenticated) {
          const Component = component as React.ComponentType<RouteComponentProps>;
          return <Component {...props} />;
        }
        return <Redirect to={{ pathname: '/login' }} />;
      }}
    />
  );
};
