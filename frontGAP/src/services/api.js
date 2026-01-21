import axios from 'axios';

// URL del Microservicio 1 (Laravel)
const AUTHORS_API = 'http://localhost:8000/api';
// URL del Microservicio 2 (Spring Boot)
const PUBLICATIONS_API = 'http://localhost:8080';

export const authorService = {
    getAll: () => axios.get(`${AUTHORS_API}/authors`),
    create: (data) => axios.post(`${AUTHORS_API}/authors`, data),
};

export const publicationService = {
    getAll: () => axios.get(`${PUBLICATIONS_API}/publications`),
    create: (data) => axios.post(`${PUBLICATIONS_API}/publications`, data),
    updateStatus: (id, status) => axios.patch(`${PUBLICATIONS_API}/publications/${id}/status`, status, {
        headers: { 'Content-Type': 'text/plain' }
    }),
};