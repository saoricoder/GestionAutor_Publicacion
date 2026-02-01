import React, { useEffect, useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  Link,
} from "react-router-dom";
import { Button } from "primereact/button";
import Login from "./components/Login";
import Authors from "./components/Authors";
import Publications from "./components/Publications";
import ErrorBoundary from "./components/ErrorBoundary";
import { useAppStore } from "./store/appStore";
import { PrimeReactProvider } from "primereact/api";
import "primereact/resources/themes/lara-light-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";
import "./styles/App.css";

/**
 * Componente protegido
 */
function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAppStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

/**
 * Layout principal
 */
function MainLayout({ children }) {
  const { user, logout } = useAppStore();

  return (
    <div className="main-layout">
      <nav className="navbar">
        <div className="navbar-brand">
          <h1>GestionAut & Pub</h1>
        </div>

        <div className="navbar-menu">
          <Link to="/authors" className="nav-link">
            Autores
          </Link>
          <Link to="/publications" className="nav-link">
            Publicaciones
          </Link>
        </div>

        <div className="navbar-end">
          <span className="user-info">{user?.email}</span>
          <Button
            icon="pi pi-sign-out"
            label="Salir"
            onClick={() => {
              logout();
              localStorage.removeItem("user");
              window.location.href = "/login";
            }}
            className="p-button-text"
          />
        </div>
      </nav>

      <main className="main-content">{children}</main>
    </div>
  );
}

/**
 * Aplicación principal
 */
export default function App() {
  const { isAuthenticated, user } = useAppStore();
  const [isReady, setIsReady] = useState(false);

  // Restaurar usuario desde localStorage al montar
  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser && !user) {
      try {
        const userData = JSON.parse(savedUser);
        const { setUser } = useAppStore.getState();
        setUser(userData);
      } catch (error) {
        console.error("Error restaurando usuario:", error);
        localStorage.removeItem("user");
      }
    }
    // Siempre marcar como listo después de intentar restaurar
    setIsReady(true);
  }, [user]);

  // Esperar a que se restaure el estado antes de renderizar
  if (!isReady) {
    return (
      <div style={{ padding: "2rem", textAlign: "center" }}>Cargando...</div>
    );
  }

  return (
    <PrimeReactProvider>
      <Router>
        <Routes>
          {/* Ruta de login - accesible sin autenticación */}
          <Route
            path="/login"
            element={
              isAuthenticated ? <Navigate to="/authors" replace /> : <Login />
            }
          />

          {/* Rutas protegidas */}
          <Route
            path="/authors"
            element={
              isAuthenticated ? (
                <ErrorBoundary>
                  <MainLayout>
                    <Authors />
                  </MainLayout>
                </ErrorBoundary>
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route
            path="/publications"
            element={
              isAuthenticated ? (
                <ErrorBoundary>
                  <MainLayout>
                    <Publications />
                  </MainLayout>
                </ErrorBoundary>
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />

          {/* Ruta por defecto - solo redirige si no está autenticado */}
          <Route
            path="/"
            element={
              isAuthenticated ? (
                <Navigate to="/authors" replace />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />

          {/* Ruta catchall para URLs no definidas */}
          <Route
            path="*"
            element={
              isAuthenticated ? (
                <Navigate to="/authors" replace />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
        </Routes>
      </Router>
    </PrimeReactProvider>
  );
}
