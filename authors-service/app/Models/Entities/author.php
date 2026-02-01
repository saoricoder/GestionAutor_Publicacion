<?php

namespace App\Models\Entities;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use App\Models\Domain\baseAuthor;

class Author extends Model implements \Illuminate\Contracts\Auth\Authenticatable
{
    use HasFactory, HasUuids;

    protected $table = 'authors';
    protected $primaryKey = 'idAuthor';
    public $incrementing = false;
    protected $keyType = 'string';
    public $timestamps = true;

    protected $fillable = [
        'name',
        'email',
        'bio',
        'affiliation',
    ];

    protected $hidden = [
        'created_at',
        'updated_at',
    ];

    // Métodos requeridos por Authenticatable
    public function getAuthIdentifierName(): string
    {
        return 'idAuthor';
    }

    public function getAuthIdentifier(): string
    {
        return $this->idAuthor;
    }

    public function getAuthPassword(): ?string
    {
        return null;
    }

    public function getAuthPasswordName(): string
    {
        return 'password';
    }

    public function getRememberToken(): ?string
    {
        return null;
    }

    public function setRememberToken($value): void
    {
        // No implementado
    }

    public function getRememberTokenName(): string
    {
        return 'remember_token';
    }

    public function getRoleDescription(): string
    {
        return "Content Creator";
    }
}
