import React, { useState, useRef } from 'react';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { Card } from 'primereact/card';
import { Toast } from 'primereact/toast';
import { useAppStore } from '../store/appStore';
import '../styles/Login.css';

/**
 * Componente de Login
 * Gestiona autenticación y redirección
 */
export default function Login() {
  const toast = useRef(null);
  const { setUser } = useAppStore();
  
  const [formData, setFormData] = useState({
    email: 'admin@example.com',
    password: 'password123'
  });
  
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Simulación de autenticación
      if (formData.email && formData.password) {
        const user = {
          id: 1,
          email: formData.email,
          name: 'Admin User',
          role: 'ADMIN'
        };
        
        setUser(user);
        localStorage.setItem('user', JSON.stringify(user));
        
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Bienvenido al sistema',
          life: 3000
        });
      }
    } catch (error) {
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <Toast ref={toast} />
      
      <Card className="login-card">
        <div className="login-header">
          <h1>GestionAut & Pub</h1>
          <p>Sistema de Gestión de Autores y Publicaciones</p>
        </div>

        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <InputText
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="admin@example.com"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <InputText
              id="password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              required
            />
          </div>

          <Button
            label="Iniciar Sesión"
            icon="pi pi-sign-in"
            loading={loading}
            type="submit"
            className="w-full"
          />
        </form>

        <div className="login-footer">
          <p>Credenciales de prueba:</p>
          <small>Email: admin@example.com</small>
          <small>Contraseña: password123</small>
        </div>
      </Card>
    </div>
  );
}
