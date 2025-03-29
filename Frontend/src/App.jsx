import './App.css'
import { Provider } from './components/ui/provider'
import Dashboard from './pages/Dashboard'

function App() {
  return (
    <Provider>
        <Dashboard/>
    </Provider>
  )
}

export default App
