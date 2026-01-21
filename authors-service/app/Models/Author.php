<?php
namespace App\Models;

use App\Domain\Entities\BaseAuthor;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Author extends Model {
    use HasFactory;

    protected $fillable = ['name', 'email', 'bio'];

    public function getRole(): string {
        return "Content Creator";
    }
}
