import React, { useState, useEffect, useRef } from 'react';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { ProgressSpinner } from 'primereact/progressspinner';
import { authorService } from '../services/api';
import { useAppStore } from '../store/appStore';
import '../styles/Authors.css';

/**
 * Componente Authors
 * Gestiona listado, creación y edición de autores
 */
export default function Authors() {
  const toast = useRef(null);
  const {
    authors,
    setAuthors,
    authorLoading,
    setAuthorLoading,
    authorError,
    setAuthorError,
    showAuthorDialog,
    setShowAuthorDialog
  } = useAppStore();

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    bio: '',
    affiliation: ''
  });

  const [editingId, setEditingId] = useState(null);
  const [globalFilter, setGlobalFilter] = useState('');

  // Cargar autores al montar
  useEffect(() => {
    loadAuthors();
  }, []);

  const loadAuthors = async () => {
    setAuthorLoading(true);
    try {
      const data = await authorService.getAll();
      setAuthors(data);
      setAuthorError(null);
    } catch (error) {
      setAuthorError(error.message);
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    } finally {
      setAuthorLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSave = async () => {
    try {
      if (!formData.name || !formData.email) {
        toast.current?.show({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Nombre y email son requeridos',
          life: 3000
        });
        return;
      }

      if (editingId) {
        await authorService.update(editingId, formData);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Autor actualizado correctamente',
          life: 3000
        });
      } else {
        await authorService.create(formData);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Autor creado correctamente',
          life: 3000
        });
      }

      resetForm();
      setShowAuthorDialog(false);
      loadAuthors();
    } catch (error) {
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    }
  };

  const handleEdit = (author) => {
    setFormData(author);
    setEditingId(author.idAuthor);
    setShowAuthorDialog(true);
  };

  const handleDelete = async (idAuthor) => {
    if (confirm('¿Está seguro de que desea eliminar este autor?')) {
      try {
        await authorService.delete(idAuthor);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Autor eliminado correctamente',
          life: 3000
        });
        loadAuthors();
      } catch (error) {
        toast.current?.show({
          severity: 'error',
          summary: 'Error',
          detail: error.message,
          life: 3000
        });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      email: '',
      bio: '',
      affiliation: ''
    });
    setEditingId(null);
  };

  const openNew = () => {
    resetForm();
    setShowAuthorDialog(true);
  };

  const actionBodyTemplate = (rowData) => {
    return (
      <div className="action-buttons">
        <Button
          icon="pi pi-pencil"
          rounded
          outlined
          severity="info"
          onClick={() => handleEdit(rowData)}
          tooltip="Editar"
        />
        <Button
          icon="pi pi-trash"
          rounded
          outlined
          severity="danger"
          onClick={() => handleDelete(rowData.idAuthor)}
          tooltip="Eliminar"
        />
      </div>
    );
  };

  return (
    <div className="authors-container">
      <Toast ref={toast} />

      <Card>
        <div className="page-header">
          <h1>Gestión de Autores</h1>
          <Button
            label="Nuevo Autor"
            icon="pi pi-plus"
            onClick={openNew}
            className="p-button-success"
          />
        </div>

        <div className="search-bar">
          <InputText
            placeholder="Buscar autor..."
            value={globalFilter}
            onChange={(e) => setGlobalFilter(e.target.value)}
            className="w-full"
          />
        </div>

        {authorLoading ? (
          <div className="loading-container">
            <ProgressSpinner />
          </div>
        ) : (
          <DataTable
            value={authors}
            globalFilter={globalFilter}
            paginator
            rows={10}
            rowsPerPageOptions={[5, 10, 20]}
            tableStyle={{ minWidth: '50rem' }}
          >
            <Column field="name" header="Nombre" sortable />
            <Column field="email" header="Email" sortable />
            <Column field="affiliation" header="Afiliación" />
            <Column field="bio" header="Biografía" />
            <Column body={actionBodyTemplate} header="Acciones" style={{ width: '120px' }} />
          </DataTable>
        )}
      </Card>

      {/* Dialog para crear/editar */}
      <Dialog
        visible={showAuthorDialog}
        onHide={() => {
          setShowAuthorDialog(false);
          resetForm();
        }}
        header={editingId ? 'Editar Autor' : 'Nuevo Autor'}
        modal
        style={{ width: '50vw' }}
      >
        <div className="form-container">
          <div className="form-group">
            <label htmlFor="name">Nombre *</label>
            <InputText
              id="name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <InputText
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleInputChange}
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="bio">Biografía</label>
            <InputText
              id="bio"
              name="bio"
              value={formData.bio}
              onChange={handleInputChange}
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="affiliation">Afiliación</label>
            <InputText
              id="affiliation"
              name="affiliation"
              value={formData.affiliation}
              onChange={handleInputChange}
              className="w-full"
            />
          </div>

          <div className="dialog-footer">
            <Button
              label="Cancelar"
              icon="pi pi-times"
              onClick={() => {
                setShowAuthorDialog(false);
                resetForm();
              }}
              className="p-button-text"
            />
            <Button
              label="Guardar"
              icon="pi pi-check"
              onClick={handleSave}
              className="p-button-success"
            />
          </div>
        </div>
      </Dialog>
    </div>
  );
}