<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Services\AuthorService;
use Illuminate\Http\Request;

class AuthorController extends Controller {
    protected $authorService;

    public function __construct(AuthorService $authorService) {
        $this->authorService = $authorService;
    }

    public function index() {
        return response()->json($this->authorService->getAllAuthors());
    }

    public function show($id) {
        return response()->json($this->authorService->getAuthorById($id));
    }

    public function store(Request $request) {
        $data = $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:authors',
            'bio' => 'nullable|string'
        ]);
        return response()->json($this->authorService->createAuthor($data), 211);
    }
}
