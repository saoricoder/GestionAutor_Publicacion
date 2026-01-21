import React, { useState, useEffect } from 'react';
import { authorService, publicationService } from '../services/api';

const Publications = () => {
    const [authors, setAuthors] = useState([]);
    const [publications, setPublications] = useState([]);
    const [form, setForm] = useState({ title: '', content: '', authorId: '' });

    useEffect(() => {
        authorService.getAll().then(res => setAuthors(res.data));
        publicationService.getAll().then(res => setPublications(res.data));
    }, []);

    const handleCreate = async () => {
        await publicationService.create(form);
        // Recargar lista...
    };

    const updateStatus = async (id, newStatus) => {
        await publicationService.updateStatus(id, newStatus);
        // Recargar lista...
    };

    return (
        <div>
            <h2>Publicaciones</h2>
            <select onChange={e => setForm({...form, authorId: e.target.value})}>
                <option value="">Seleccione Autor</option>
                {authors.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
            </select>
            {/* Botones para cambiar estados: DRAFT -> IN_REVIEW -> APPROVED [cite: 45] */}
            <table>
                {publications.map(p => (
                    <tr key={p.id}>
                        <td>{p.title}</td>
                        <td>{p.status}</td>
                        <td>
                            <button onClick={() => updateStatus(p.id, 'APPROVED')}>Aprobar</button>
                            <button onClick={() => updateStatus(p.id, 'REJECTED')}>Rechazar</button>
                        </td>
                    </tr>
                ))}
            </table>
        </div>
    );
};