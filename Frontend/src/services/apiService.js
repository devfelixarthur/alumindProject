import axios from 'axios';
import Swal from 'sweetalert2';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

const handleError = (error) => {
  if (error.response) {
    const status = error.response.status;
    if (status === 400) {
      Swal.fire({
        title: 'Atenção!',
        text: 'Houve um erro na requisição. Por favor, verifique os parâmetros enviados.',
        icon: 'warning',
        confirmButtonText: 'OK',
      });
    } else if (status === 404) {
      Swal.fire({
        title: 'Aviso!',
        text: 'Dados não encontrados para os parâmetros fornecidos.',
        icon: 'warning',
        confirmButtonText: 'OK',
      });
    } else if (status === 500) {
      Swal.fire({
        title: 'Erro!',
        text: 'Houve um erro no servidor. Tente novamente mais tarde.',
        icon: 'error',
        confirmButtonText: 'OK',
      });
    } else {
      Swal.fire({
        title: 'Erro!',
        text: 'Houve um erro inesperado. Tente novamente mais tarde.',
        icon: 'error',
        confirmButtonText: 'OK',
      });
    }
  } else if (error.request) {
    Swal.fire({
      title: 'Erro!',
      text: 'Não foi possível se conectar ao servidor. Verifique sua conexão e tente novamente.',
      icon: 'error',
      confirmButtonText: 'OK',
    });
  } else {
    Swal.fire({
      title: 'Erro!',
      text: `Erro: ${error.message}`,
      icon: 'error',
      confirmButtonText: 'OK',
    });
  }
};

export const fetchData = async () => {
  try {
    const response = await api.get('/feedbacks');
    return response.data;
  } catch (error) {
    handleError(error); 
    throw error;
  }
};

export const postData = async (data) => {
  try {
    const response = await api.post('/feedbacks', data);
    return response.data;
  } catch (error) {
    handleError(error);
    throw error;
  }
};

export const putData = async (id, data) => {
  try {
    const response = await api.put(`/feedbacks/${id}`, data);
    return response.data;
  } catch (error) {
    handleError(error);
    throw error;
  }
};

export const deleteData = async (id) => {
  try {
    const response = await api.delete(`/feedbacks/${id}`); 
    return response.data;
  } catch (error) {
    handleError(error);
    throw error;
  }
};
