import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
//import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { Provider } from 'react-redux'

import Store from './app/Store.js'

import { Buffer } from "buffer";


window.global = window;
window.Buffer = Buffer;


createRoot(document.getElementById('root')).render(
  <BrowserRouter>
      <Provider store={Store}>
          <App />
      </Provider>
  </BrowserRouter>
)
