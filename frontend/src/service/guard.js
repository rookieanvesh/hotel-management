
// src/ProtectedRoute.js
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import ApiService from './ApiService';

//This code defines a ProtectedRoute component that checks if the user is authenticated using 
//ApiService.isAuthenticated(). If authenticated, it renders the provided Component. 
//If not, it redirects the user to the /login page, preserving the current location in the URL state.
export const ProtectedRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isAuthenticated() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};


export const AdminRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isAdmin() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};