import React from 'react';
import { useLoading } from '../../context/LoadingContext';
import { CircularProgress, Box } from '@mui/material';

const LoadingScreen = () => {
  const { loading } = useLoading();

  if (!loading) return null;

  return (
    <Box
      sx={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: 9999,
      }}
    >
      <CircularProgress color="secondary" size={100} sx={{ color: '#1a009e' }}/>
    </Box>
  );
};

export default LoadingScreen;
