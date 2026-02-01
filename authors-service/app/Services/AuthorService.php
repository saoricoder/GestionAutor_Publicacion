<?php

namespace App\Services;

use App\Repositories\Interfaces\AuthorRepositoryInterface;
use App\Models\Domain\RegularAuthor;
use App\DTOs\AuthorDTO;

class AuthorService
{
    public function __construct(protected AuthorRepositoryInterface $repository) {}

    public function listAuthors()
    {
        return $this->repository->getAll()->map(fn($a) => $this->mapToDTO($a));
    }

    public function findAuthor(string $id)
    {
        return $this->mapToDTO($this->repository->find($id));
    }

    public function createAuthor(array $data)
    {
        $author = $this->repository->store($data);
        return $this->mapToDTO($author);
    }

    private function mapToDTO($entity)
    {
        $domain = new RegularAuthor(
            $entity->idAuthor,
            $entity->name,
            $entity->email,
            $entity->bio,
            $entity->affiliation,
            $entity->created_at
        );
        return AuthorDTO::fromDomain($domain, $entity->affiliation);
    }

        public function updateAuthor($id, array $data)
    {
        return $this->repository->update($id, $data);
    }

    public function deleteAuthor($id) {
        return $this->repository->delete($id);
    }
}
