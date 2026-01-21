import React, { useState, useEffect } from 'react';
import { authorService } from '../services/api';

const Authors = () => {
    const [authors, setAuthors] = useState([]);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');

    const loadAuthors = async () => {
        const res = await authorService.getAll();
        setAuthors(res.data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await authorService.create({ name, email });
        loadAuthors();
    };

    useEffect(() => { loadAuthors(); }, []);

    return (
        <div>
            <h2>Gestión de Autores</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Nombre" onChange={e => setName(e.target.value)} />
                <input placeholder="Email" onChange={e => setEmail(e.target.value)} />
                <button type="submit">Guardar Autor</button>
            </form>
            <ul>
                {authors.map(a => <li key={a.id}>{a.name} ({a.email})</li>)}
            </ul>
        </div>
    );
};