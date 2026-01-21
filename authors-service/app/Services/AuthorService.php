<?php
namespace App\Services;
/**La lógica de negocio reside aquí, siguiendo el principio de responsabilidad única (SRP) */
use App\Repositories\Interfaces\AuthorRepositoryInterface;

class AuthorService {
    protected $repository;

    public function __construct(AuthorRepositoryInterface $repository) {
        $this->repository = $repository;
    }

    public function getAllAuthors() {
        return $this->repository->all();
    }

    public function getAuthorById($id) {
        return $this->repository->find($id);
    }

    public function createAuthor(array $data) {
        // Aquí podrías agregar validaciones extra de negocio
        return $this->repository->create($data);
    }
}
