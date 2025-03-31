import Swal from 'sweetalert2';

export const validateDateFilters = (startDate, endDate, range) => {
  if (startDate && !endDate) {
    Swal.fire({
      toast: true,
      position: 'top-start',
      title: 'Atenção!',
      text: 'Se a data de início for informada, a data de término também deve ser informada.',
      icon: 'warning',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      background: '#FFF9C4',
      didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '120px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }
    });
    return false;
  }

  if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
    Swal.fire({
      toast: true,
      position: 'top-start',
      title: 'Atenção!',
      text: 'A data de início não pode ser maior que a data de término.',
      icon: 'warning',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      background: '#FFF9C4',
      didOpen: () => {
        const swalPopup = document.querySelector('.swal2-popup');
        swalPopup.style.height = '120px';
        swalPopup.style.padding = '15px 20px';
        swalPopup.style.minWidth = '200px';
      }
    });
    return false;
  }

  if (startDate && endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end - start);
    const diffDays = diffTime / (1000 * 60 * 60 * 24); 

    if (diffDays > range) {
      Swal.fire({
        toast: true,
        position: 'top-start',
        title: 'Atenção!',
        text: 'A diferença entre a data de início e a data de término não pode ser superior a 30 dias.',
        icon: 'warning',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        background: '#FFF9C4',
        didOpen: () => {
            const swalPopup = document.querySelector('.swal2-popup');
            swalPopup.style.height = '130px';
            swalPopup.style.padding = '10px 10px';
            swalPopup.style.minWidth = '250px';
          }
      });
      return false;
    }
  }
  return true;
};
