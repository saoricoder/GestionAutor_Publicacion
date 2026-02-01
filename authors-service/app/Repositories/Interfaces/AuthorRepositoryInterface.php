<?php

namespace App\Repositories\Interfaces;

interface AuthorRepositoryInterface
{
    public function getAll();
    public function find(string $idAuthor);
    public function getById(int $idAuthor);
    public function store(array $data);
    public function delete($id);
}
