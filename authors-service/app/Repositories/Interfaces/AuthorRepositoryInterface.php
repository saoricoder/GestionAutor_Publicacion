<?php
namespace App\Repositories\Interfaces;

interface AuthorRepositoryInterface {
    public function all();
    public function find($id);
    public function create(array $data);
}
