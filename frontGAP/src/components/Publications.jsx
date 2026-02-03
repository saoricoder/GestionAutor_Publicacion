import React, { useState, useEffect, useRef } from 'react';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { InputTextarea } from 'primereact/inputtextarea';
import { Dropdown } from 'primereact/dropdown';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import { Toast } from 'primereact/toast';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';
import { ProgressSpinner } from 'primereact/progressspinner';
import { publicationService, authorService } from '../services/api';
import { useAppStore } from '../store/appStore';
import '../styles/Publications.css';

const STATUS_OPTIONS = [
  { label: 'Borrador', value: 'DRAFT' },
  { label: 'En Revisión', value: 'IN_REVIEW' },
  { label: 'Aprobado', value: 'APPROVED' },
  { label: 'Publicado', value: 'PUBLISHED' },
  { label: 'Rechazado', value: 'REJECTED' }
];

const STATUS_COLORS = {
  'DRAFT': 'warning',
  'IN_REVIEW': 'info',
  'APPROVED': 'success',
  'PUBLISHED': 'success',
  'REJECTED': 'danger'
};

/**
 * Componente Publications
 * Gestiona listado, creación, edición y cambio de estado de publicaciones
 * STRATEGY PATTERN: Backend valida transiciones de estado
 */
export default function Publications() {
  const toast = useRef(null);
  const {
    publications,
    setPublications,
    selectedPublication,
    setSelectedPublication,
    publicationLoading,
    setPublicationLoading,
    showPublicationDialog,
    setShowPublicationDialog,
    showPublicationDetail,
    setShowPublicationDetail
  } = useAppStore();

  const [authors, setAuthors] = useState([]);
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    authorId: '',
    status: 'DRAFT'
  });

  const [editingId, setEditingId] = useState(null);
  const [globalFilter, setGlobalFilter] = useState('');
  const [publicationError, setPublicationError] = useState(null);

  // Cargar datos al montar
  useEffect(() => {
    loadPublications();
    loadAuthors();
  }, []);

  const loadPublications = async () => {
    setPublicationLoading(true);
    setPublicationError(null);
    try {
      const data = await publicationService.getAll();
      setPublications(data);
    } catch (error) {
      setPublicationError(error.message || 'Error al cargar publicaciones');
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    } finally {
      setPublicationLoading(false);
    }
  };

  const loadAuthors = async () => {
    try {
      const data = await authorService.getAll();
      setAuthors(data);
    } catch (error) {
      console.error('Error cargando autores:', error);
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
      if (!formData.title || !formData.content || !formData.authorId) {
        toast.current?.show({
          severity: 'warn',
          summary: 'Validación',
          detail: 'Título, contenido y autor son requeridos',
          life: 3000
        });
        return;
      }

      if (editingId) {
        await publicationService.update(editingId, formData);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Publicación actualizada correctamente',
          life: 3000
        });
      } else {
        await publicationService.create(formData);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Publicación creada correctamente',
          life: 3000
        });
      }

      resetForm();
      setShowPublicationDialog(false);
      loadPublications();
    } catch (error) {
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    }
  };

  const handleEdit = (publication) => {
    setFormData({
      title: publication.title,
      content: publication.content,
      authorId: publication.author?.id || '',
      status: publication.status
    });
    setEditingId(publication.idPublication);
    setShowPublicationDialog(true);
  };

  const handleDelete = async (idPublication) => {
    if (confirm('¿Está seguro de que desea eliminar esta publicación?')) {
      try {
        await publicationService.delete(idPublication);
        toast.current?.show({
          severity: 'success',
          summary: 'Éxito',
          detail: 'Publicación eliminada correctamente',
          life: 3000
        });
        loadPublications();
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

  const handleShowDetail = async (publication) => {
    try {
      const detail = await publicationService.getById(publication.idPublication);
      setSelectedPublication(detail);
      setShowPublicationDetail(true);
    } catch (error) {
      toast.current?.show({
        severity: 'error',
        summary: 'Error',
        detail: error.message,
        life: 3000
      });
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      content: '',
      authorId: '',
      status: 'DRAFT'
    });
    setEditingId(null);
  };

  const openNew = () => {
    resetForm();
    setShowPublicationDialog(true);
  };

  const statusBodyTemplate = (rowData) => {
    return (
      <Tag
        value={rowData.status}
        severity={STATUS_COLORS[rowData.status]}
      />
    );
  };

  const actionBodyTemplate = (rowData) => {
    return (
      <div className="action-buttons">
        <Button
          icon="pi pi-eye"
          rounded
          outlined
          severity="info"
          onClick={() => handleShowDetail(rowData)}
          tooltip="Ver detalles"
        />
        <Button
          icon="pi pi-pencil"
          rounded
          outlined
          severity="warning"
          onClick={() => handleEdit(rowData)}
          tooltip="Editar"
        />
        <Button
          icon="pi pi-trash"
          rounded
          outlined
          severity="danger"
          onClick={() => handleDelete(rowData.idPublication)}
          tooltip="Eliminar"
        />
      </div>
    );
  };

  return (
    <div className="publications-container">
      <Toast ref={toast} />

      <Card>
        <div className="page-header">
          <h1>Gestión de Publicaciones</h1>
          <Button
            label="Nueva Publicación"
            icon="pi pi-plus"
            onClick={openNew}
            className="p-button-success"
          />
        </div>

        <div className="search-bar">
          <InputText
            placeholder="Buscar publicación..."
            value={globalFilter}
            onChange={(e) => setGlobalFilter(e.target.value)}
            className="w-full"
          />
        </div>

        {publicationLoading ? (
          <div className="loading-container">
            <ProgressSpinner />
          </div>
        ) : publicationError ? (
          <div className="error-container">
            <Card>
              <div className="error-content">
                <i className="pi pi-exclamation-circle error-icon"></i>
                <h2>Error al cargar publicaciones</h2>
                <p>{publicationError}</p>
                <p className="error-detail">El servicio de publicaciones no está disponible en este momento.</p>
                <Button 
                  label="Reintentar" 
                  icon="pi pi-refresh"
                  onClick={loadPublications}
                  className="p-button-warning"
                />
              </div>
            </Card>
          </div>
        ) : (
          <DataTable
            value={publications}
            globalFilter={globalFilter}
            paginator
            rows={10}
            rowsPerPageOptions={[5, 10, 20]}
            tableStyle={{ minWidth: '50rem' }}
          >
            <Column field="title" header="Título" sortable />
            <Column
              field="author.name"
              header="Autor"
              body={(rowData) => rowData.author?.name || 'N/A'}
              sortable
            />
            <Column body={statusBodyTemplate} header="Estado" />
            <Column
              field="createdDate"
              header="Creado"
              body={(rowData) => new Date(rowData.createdDate).toLocaleDateString()}
            />
            <Column body={actionBodyTemplate} header="Acciones" style={{ width: '150px' }} />
          </DataTable>
        )}
      </Card>

      {/* Dialog para crear/editar */}
      <Dialog
        visible={showPublicationDialog}
        onHide={() => {
          setShowPublicationDialog(false);
          resetForm();
        }}
        header={editingId ? 'Editar Publicación' : 'Nueva Publicación'}
        modal
        style={{ width: '50vw' }}
      >
        <div className="form-container">
          <div className="form-group">
            <label htmlFor="title">Título *</label>
            <InputText
              id="title"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="authorId">Autor *</label>
            <Dropdown
              id="authorId"
              name="authorId"
              value={formData.authorId}
              onChange={(e) => setFormData(prev => ({ ...prev, authorId: e.value }))}
              options={authors}
              optionLabel="name"
              optionValue="id"
              placeholder="Seleccionar autor"
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="content">Contenido *</label>
            <InputTextarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleInputChange}
              rows={8}
              className="w-full"
            />
          </div>

          <div className="form-group">
            <label htmlFor="status">Estado</label>
            <Dropdown
              id="status"
              name="status"
              value={formData.status}
              onChange={(e) => setFormData(prev => ({ ...prev, status: e.value }))}
              options={STATUS_OPTIONS}
              optionLabel="label"
              optionValue="value"
              placeholder="Seleccionar estado"
              className="w-full"
            />
          </div>

          <div className="dialog-footer">
            <Button
              label="Cancelar"
              icon="pi pi-times"
              onClick={() => {
                setShowPublicationDialog(false);
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

      {/* Dialog para detalles */}
      <Dialog
        visible={showPublicationDetail}
        onHide={() => setShowPublicationDetail(false)}
        header="Detalles de Publicación"
        modal
        style={{ width: '60vw' }}
      >
        {selectedPublication && (
          <div className="detail-container">
            <div className="detail-section">
              <h3>{selectedPublication.title}</h3>
              <Tag
                value={selectedPublication.status}
                severity={STATUS_COLORS[selectedPublication.status]}
              />
            </div>

            <div className="detail-section">
              <h4>Autor</h4>
              <p><strong>Nombre:</strong> {selectedPublication.author?.name}</p>
              <p><strong>Email:</strong> {selectedPublication.author?.email}</p>
              <p><strong>Afiliación:</strong> {selectedPublication.author?.affiliation || 'N/A'}</p>
            </div>

            <div className="detail-section">
              <h4>Contenido</h4>
              <p>{selectedPublication.content}</p>
            </div>

            <div className="dialog-footer">
              <Button
                label="Cerrar"
                icon="pi pi-times"
                onClick={() => setShowPublicationDetail(false)}
              />
            </div>
          </div>
        )}
      </Dialog>
    </div>
  );
}