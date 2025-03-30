import React, { useState, useEffect } from 'react';
import { Modal, Box, Button, Typography, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

// Mock API request function
const mockFetchFeedbackDetails = (id) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (Math.random() > 0.1) { // 80% chance of success
        resolve({
          id,
          originalFeedback: "Tenho usado o Alumind há algum tempo. Ele funciona bem, mas não me empolga muito.",
          sentiment: "NEUTRO",
          requestedFeatures: [
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
            { code: "RECURSO_NOVIDADE", reason: "Implementar novos recursos para tornar o Alumind mais atraente." },
            { code: "EXPERIENCIA_USUARIO", reason: "Melhorar a experiência do usuário para torná-la mais agradável." },
          ],
          dtRegister: "29/03/2025 14:03:22",
        });
      } else {
        reject(new Error('Erro ao carregar os dados, entre com contato com o suporte.'));
      }
    }, 1000);
  });
};

// Modal Component
const ModalDetails = ({ id, open, onClose }) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (open) {
        setData(null);
        setError(null);
        setLoading(true); 
  
        if (id) {
          mockFetchFeedbackDetails(id)
            .then((data) => {
              setData(data);
              setLoading(false); 
            })
            .catch((error) => {
              setError(error.message);
              setLoading(false); 
            });
        }
      }
    }, [open, id]);

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
        <Typography id="modal-title" variant="h6" component="h2">
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
              <Typography variant="body1"><strong>Feedback Original:</strong> {data.originalFeedback}</Typography>
              <Typography variant="body1"><strong>Sentimento:</strong> {data.sentiment}</Typography>
              <Typography variant="body1"><strong>Data de Registro:</strong> {data.dtRegister}</Typography>

              {/* Requested Features Table */}
              {data.requestedFeatures && data.requestedFeatures.length > 0 && (
                <Box sx={{ 
                    mt: 2,
                    overflowY: 'auto',
                    maxHeight: '300px',
                 }}>
                  <Typography variant="h6">Sugestões de Melhorias</Typography>
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
