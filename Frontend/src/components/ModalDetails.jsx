import React, { useState, useEffect } from 'react';
import { Modal, Box, Button, Typography, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { getFeedbackDetails } from '@/services/apiService';

const ModalDetails = ({ id, open, onClose }) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (id && open) {
      setLoading(true);
      setError(null);
      setData(null);

      getFeedbackDetails(id)
        .then((response) => {
          setData(response);
          setLoading(false);
        })
        .catch(() => {
            onClose();
        });
    }
  }, [id, open]);

  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="modal-title"
      aria-describedby="modal-description"
    >
      <Box
        sx={{
          overflowY: 'auto',
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: 900,
          maxHeight: '700px',
          bgcolor: 'background.paper',
          boxShadow: 24,
          p: 4,
        }}
      >
        <Typography id="modal-title" variant="h6" component="h2" fontWeight="bold">
          Detalhes do Feedback
        </Typography>

        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress />
          </Box>
        )}

        {error && (
          <Typography variant="body2" color="error" sx={{ mt: 2 }}>
            {error}
          </Typography>
        )}

        {data && (
          <>
            <Box sx={{ mt: 2 }}>
              <Typography variant="body1"><strong>ID:</strong> {data.id}</Typography>
              <Typography variant="body1"><strong>Feedback:</strong> {data.originalFeedback}</Typography>
              <Typography variant="body1"><strong>Sentimento:</strong> {data.sentiment}</Typography>
              <Typography variant="body1"><strong>Data de Registro:</strong> {data.dtRegister}</Typography>

              {data.requestedFeatures && data.requestedFeatures.length > 0 && (
                <Box sx={{ 
                    mt: 2,
                    overflowY: 'auto',
                    maxHeight: '300px',
                 }}>
                  <Typography variant="h6" fontWeight="bold">Análise de Melhorias</Typography>
                  <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 350 }} aria-label="features table">
                      <TableHead>
                        <TableRow>
                          <TableCell>Código</TableCell>
                          <TableCell>Razão</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {data.requestedFeatures.map((feature, index) => (
                          <TableRow key={index}>
                            <TableCell>{feature.code}</TableCell>
                            <TableCell>{feature.reason}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Box>
              )}
            </Box>

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                <Button
                    variant="contained"
                    onClick={onClose}
                    sx={{ backgroundColor: 'black', color: 'white', '&:hover': { backgroundColor: 'gray.200' } }}
                    >
                    Fechar
                </Button>            
            </Box>
          </>
        )}
      </Box>
    </Modal>
  );
};

export default ModalDetails;
