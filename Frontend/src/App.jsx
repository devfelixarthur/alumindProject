import './App.css'
import LoadingScreen from './components/ui/LoadingScreen'
import { Provider } from './components/ui/provider'
import { LoadingProvider } from './context/LoadingContext';
import Dashboard from './pages/Dashboard'

function App() {
  return (
    <Provider>
      <LoadingProvider>
        <LoadingScreen />
        <Dashboard/>
      </LoadingProvider>
    </Provider>
  )
}

export default App
