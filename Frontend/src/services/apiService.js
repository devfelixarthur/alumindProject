import { startLoading, stopLoading } from '@/context/LoadingController';
import axios from 'axios';
import Swal from 'sweetalert2';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
    startLoading();
    return config;
  }, (error) => {
    stopLoading();
    return Promise.reject(error);
  });
  
  api.interceptors.response.use((response) => {
    stopLoading(); 
    return response;
  }, (error) => {
    stopLoading();
    handleError(error);
    return Promise.reject(error);
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
        background: '#FFF9C4',
        didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }
      });
    } else if (status === 404) {
      Swal.fire({
        toast: true,
        position: 'top-start',
        title: 'Atenção!',
        text: 'Dados não encontrados para os parâmetros fornecidos.',
        icon: 'warning',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        background: '#FFF9C4',
        didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }
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
        background: '#FFAAA5', 
        didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      } 
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
        background: '#FFAAA5', 
        didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }   
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
      background: '#FFAAA5',
      didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }
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
      background: '#FFAAA5',
      didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '130px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }    
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
    try {
      const response = await api.get(`/feedbacks/semanalMetrics?dtStart=${formatDate(startDate)}&dtEnd=${formatDate(endDate)}`);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  };

  export const fetchFeedbacks = async (params) => {
    if (params.dtStart != null) {
      params.dtStart = formatDate(params.dtStart);
    }
    
    if (params.dtEnd != null) {
      params.dtEnd = formatDate(params.dtEnd);
    }
  
    try {
      const response = await api.get('/feedbacks/searchFeedbacksByFields', { params });
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  };
  

  export const formatDate = (dateString) => {
    const datePattern = /^\d{4}-\d{2}-\d{2}$/;
  
    if (datePattern.test(dateString)) {
      const year = dateString.substring(0, 4);
      const month = dateString.substring(5, 7);
      const day = dateString.substring(8, 10);
      return `${day}/${month}/${year}`;
    }
  
    return dateString;
  };
  
