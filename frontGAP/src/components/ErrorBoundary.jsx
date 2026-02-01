import React from "react";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import "../styles/ErrorBoundary.css";

/**
 * Error Boundary Component
 * Captura errores de componentes hijos
 */
export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("Error capturado por ErrorBoundary:", error, errorInfo);
  }

  handleReset = () => {
    this.setState({ hasError: false, error: null });
  };

  render() {
    if (this.state.hasError) {
      return (
        <div className="error-boundary-container">
          <Card>
            <div className="error-boundary-content">
              <i className="pi pi-exclamation-triangle error-boundary-icon"></i>
              <h2>Algo salió mal</h2>
              <p>Se ha producido un error inesperado.</p>
              <p className="error-details">{this.state.error?.message}</p>
              <Button
                label="Recargar página"
                icon="pi pi-refresh"
                onClick={() => window.location.reload()}
                className="p-button-primary"
              />
            </div>
          </Card>
        </div>
      );
    }

    return this.props.children;
  }
}
