import { loadingControl } from './LoadingContext'

export const startLoading = () => loadingControl.start()
export const stopLoading = () => loadingControl.stop()
