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
        toast: true,
        position: 'top-start',
        title: 'Atenção!',
        text: 'Houve um erro na requisição. Por favor, verifique os parâmetros enviados.',
        icon: 'warning',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
      });
    } else if (status === 404) {
      Swal.fire({
        toast: true,
        position: 'top-start',
        title: 'Aviso!',
        text: 'Dados não encontrados para os parâmetros fornecidos.',
        icon: 'warning',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
      });
    } else if (status === 500) {
      Swal.fire({
        toast: true,
        position: 'top-start',
        title: 'Erro!',
        text: 'Houve um erro no servidor. Tente novamente mais tarde.',
        icon: 'error',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,      
    });
    } else {
      Swal.fire({
        toast: true,
        position: 'top-start',
        title: 'Erro!',
        text: 'Houve um erro inesperado. Tente novamente mais tarde.',
        icon: 'error',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,      
      });
    }
  } else if (error.request) {
    Swal.fire({
      toast: true,
      position: 'top-start',
      title: 'Erro!',
      text: 'Não foi possível se conectar ao servidor. Verifique sua conexão e tente novamente.',
      icon: 'error',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,      
    });
  } else {
    Swal.fire({
      toast: true,
      position: 'top-start',
      title: 'Erro!',
      text: `Erro: ${error.message}`,
      icon: 'error',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,      
    });
  }
};

export const getFeedbackDetails = async (id) => {
    try {
      const response = await api.get(`/feedbacks/feedbackDetails/${id}`);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  };

  export const fetchWeeklyMetrics = async (startDate, endDate) => {
    console.log(startDate)
    console.log(endDate)
    try {
      const response = await api.get(`/feedbacks/semanalMetrics?dtStart=${formatDate(startDate)}&dtEnd=${formatDate(endDate)}`);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  };

  

  const formatDate = (dateString) => {
    console.log(dateString)
    const year = dateString.substring(0, 4);
    const month = dateString.substring(5, 7);
    const day = dateString.substring(8, 10);
    console.log(`${day}/${month}/${year}`)
    return `${day}/${month}/${year}`;
  };
