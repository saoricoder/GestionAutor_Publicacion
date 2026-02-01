import { create } from 'zustand';

/**
 * ESTADO GLOBAL DE LA APLICACIÓN
 * Gestiona: usuario, autores, publicaciones, UI states
 */
export const useAppStore = create((set) => ({
  // === AUTH ===
  user: null,
  isAuthenticated: false,
  
  setUser: (user) => set({ user, isAuthenticated: !!user }),
  logout: () => set({ user: null, isAuthenticated: false }),
  
  // === AUTORES ===
  authors: [],
  selectedAuthor: null,
  authorLoading: false,
  authorError: null,
  
  setAuthors: (authors) => set({ authors }),
  setSelectedAuthor: (author) => set({ selectedAuthor: author }),
  setAuthorLoading: (loading) => set({ authorLoading: loading }),
  setAuthorError: (error) => set({ authorError: error }),
  
  // === PUBLICACIONES ===
  publications: [],
  selectedPublication: null,
  publicationLoading: false,
  publicationError: null,
  
  setPublications: (publications) => set({ publications }),
  setSelectedPublication: (publication) => set({ selectedPublication: publication }),
  setPublicationLoading: (loading) => set({ publicationLoading: loading }),
  setPublicationError: (error) => set({ publicationError: error }),
  
  // === UI ===
  showAuthorDialog: false,
  showPublicationDialog: false,
  showPublicationDetail: false,
  
  setShowAuthorDialog: (show) => set({ showAuthorDialog: show }),
  setShowPublicationDialog: (show) => set({ showPublicationDialog: show }),
  setShowPublicationDetail: (show) => set({ showPublicationDetail: show }),
}));