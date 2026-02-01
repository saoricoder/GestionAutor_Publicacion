<?php

namespace App\Repositories\Eloquent;

use App\Models\Entities\Author;
use App\Repositories\Interfaces\AuthorRepositoryInterface;
use Illuminate\Support\Str;

class AuthorRepository implements AuthorRepositoryInterface
{
    public function getAll()
    {
        return Author::all();
    }
    public function getById(int $idAuthor)
    {
        return Author::findOrFail($idAuthor)->firstOrFail();
    }
    public function find(string $idAuthor)
    {
        return Author::where('idAuthor', $idAuthor)->firstOrFail();
    }

    public function store(array $data)
    {
        return Author::create($data);
    }
        public function delete($id) {
        return Author::destroy($id);
    }
        public function update($id, array $data)
    {
        $author = Author::find($id);
        if ($author) {
            $author->update($data);
        }
        return $author;
    }
}
