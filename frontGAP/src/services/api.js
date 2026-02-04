import axios from "axios";

const AUTHORS_API = import.meta.env.VITE_AUTHORS_API_URL || "http://localhost:8000/api";
const PUBLICATIONS_API = import.meta.env.VITE_PUBLICATIONS_API_URL || "http://localhost:8080/api";

/**
 * ADAPTER PATTERN: Adaptador para comunicación con APIs
 */

// ============ AUTORES ============
export const authorService = {
  /**
   * Obtener todos los autores
   */
  getAll: async () => {
    try {
      const response = await axios.get(`${AUTHORS_API}/authors`);
      return response.data.data || [];
    } catch (error) {
      console.error("Error obteniendo autores:", error);
      throw {
        message: error.response?.data?.message || "Error al obtener autores",
        code: error.response?.status,
      };
    }
  },

  /**
   * Obtener un autor específico
   */
  getById: async (idAuthor) => {
    try {
      const response = await axios.get(`${AUTHORS_API}/authors/${idAuthor}`);
      return response.data.data;
    } catch (error) {
      console.error("Error obteniendo autor:", error);
      throw {
        message: error.response?.data?.message || "Autor no encontrado",
        code: error.response?.status,
      };
    }
  },

  /**
   * Crear un nuevo autor
   */
  create: async (data) => {
    try {
      const response = await axios.post(`${AUTHORS_API}/authors`, data);
      return response.data.data;
    } catch (error) {
      console.error("Error creando autor:", error);
      throw {
        message: error.response?.data?.message || "Error al crear autor",
        errors: error.response?.data?.errors,
        code: error.response?.status,
      };
    }
  },

  /**
   * Actualizar un autor
   */
  update: async (idAuthor, data) => {
    try {
      const response = await axios.put(
        `${AUTHORS_API}/authors/${idAuthor}`,
        data,
      );
      return response.data.data;
    } catch (error) {
      console.error("Error actualizando autor:", error);
      throw {
        message: error.response?.data?.message || "Error al actualizar autor",
        code: error.response?.status,
      };
    }
  },

  /**
   * Eliminar un autor
   */
  delete: async (idAuthor) => {
    try {
      await axios.delete(`${AUTHORS_API}/authors/${idAuthor}`);
      return true;
    } catch (error) {
      console.error("Error eliminando autor:", error);
      throw {
        message: error.response?.data?.message || "Error al eliminar autor",
        code: error.response?.status,
      };
    }
  },
};

// ============ PUBLICACIONES ============
export const publicationService = {
  /**
   * Obtener todas las publicaciones
   * @param {string} idAuthor - Filtro opcional por autor
   */
  getAll: async (idAuthor = null) => {
    try {
      const url = idAuthor
        ? `${PUBLICATIONS_API}/publications?idAuthor=${idAuthor}`
        : `${PUBLICATIONS_API}/publications`;

      const response = await axios.get(url);
      return response.data.data || [];
    } catch (error) {
      console.error("Error obteniendo publicaciones:", error);
      throw {
        message:
          error.response?.data?.message || "Error al obtener publicaciones",
        code: error.response?.status,
      };
    }
  },

  /**
   * Obtener una publicación específica con datos del autor
   */
  getById: async (idPublication) => {
    try {
      const response = await axios.get(
        `${PUBLICATIONS_API}/publications/${idPublication}`,
      );
      return response.data.data;
    } catch (error) {
      console.error("Error obteniendo publicación:", error);
      throw {
        message: error.response?.data?.message || "Publicación no encontrada",
        code: error.response?.status,
      };
    }
  },

  /**
   * Crear una nueva publicación
   * Valida que el autor exista en el backend
   */
  create: async (data) => {
    try {
      const response = await axios.post(
        `${PUBLICATIONS_API}/publications`,
        data,
      );
      return response.data.data;
    } catch (error) {
      console.error("Error creando publicación:", error);
      throw {
        message: error.response?.data?.message || "Error al crear publicación",
        errors: error.response?.data?.errors,
        code: error.response?.status,
      };
    }
  },

  /**
   * Cambiar estado editorial de una publicación
   * STRATEGY PATTERN: El backend valida transiciones permitidas
   */
  updateStatus: async (idPublication, newStatus) => {
    try {
      const response = await axios.patch(
        `${PUBLICATIONS_API}/publications/${idPublication}/status`,
        { newStatus },
      );
      return response.data.data;
    } catch (error) {
      console.error("Error actualizando estado:", error);
      throw {
        message: error.response?.data?.message || "Error al cambiar estado",
        code: error.response?.status,
      };
    }
  },

  /**
   * Actualizar datos de una publicación
   */
  update: async (idPublication, data) => {
    try {
      const response = await axios.put(
        `${PUBLICATIONS_API}/publications/${idPublication}`,
        data,
      );
      return response.data.data;
    } catch (error) {
      console.error("Error actualizando publicación:", error);
      throw {
        message:
          error.response?.data?.message || "Error al actualizar publicación",
        code: error.response?.status,
      };
    }
  },

  /**
   * Actualizar estado de publicación
   */
  updateStatus: async (idPublication, status) => {
    try {
      const response = await axios.patch(
        `${PUBLICATIONS_API}/publications/${idPublication}/status`,
        { status },
      );
      return response.data.data;
    } catch (error) {
      console.error("Error actualizando estado:", error);
      throw {
        message:
          error.response?.data?.message || "Error al actualizar estado",
        code: error.response?.status,
      };
    }
  },

  /**
   * Eliminar una publicación
   */
  delete: async (idPublication) => {
    try {
      await axios.delete(`${PUBLICATIONS_API}/publications/${idPublication}`);
      return true;
    } catch (error) {
      console.error("Error eliminando publicación:", error);
      throw {
        message:
          error.response?.data?.message || "Error al eliminar publicación",
        code: error.response?.status,
      };
    }
  },
};

// ============ UTILIDADES ============
export const setAuthToken = (token) => {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common["Authorization"];
  }
};