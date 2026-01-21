<?php
namespace App\Repositories\Eloquent;

use App\Models\Author;
use App\Repositories\Interfaces\AuthorRepositoryInterface;

class AuthorRepository implements AuthorRepositoryInterface {
    public function all() {
        return Author::all(); [cite: 31]
    }

    public function find($id) {
        return Author::findOrFail($id); [cite: 30]
    }

    public function create(array $data) {
        return Author::create($data); [cite: 29]
    }
}
