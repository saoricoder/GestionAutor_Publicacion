<?php

namespace App\Http\Controllers;

use App\Services\AuthorService;
use App\Http\Requests\storeAuthorRequest;
use Illuminate\Http\JsonResponse;

class AuthorController
{
    public function __construct(protected AuthorService $service) {}

    public function index(): JsonResponse
    {
        return response()->json([
            'success' => true,
            'data' => $this->service->listAuthors()
        ]);
    }

    public function show(string $id): JsonResponse
    {
        return response()->json([
            'success' => true,
            'data' => $this->service->findAuthor($id)
        ]);
    }

    public function store(storeAuthorRequest $request): JsonResponse
    {
        return response()->json([
            'success' => true,
            'data' => $this->service->createAuthor($request->validated())
        ], 201);
    }

    public function update(storeAuthorRequest $request, string $id): JsonResponse
    {
        return response()->json([
            'success' => true,
            'data' => $this->service->updateAuthor($id, $request->validated())
        ]);
    }

    public function destroy($id): JsonResponse
    {
        $this->service->deleteAuthor($id);
        return response()->json([
            'success' => true,
            'message' => 'Autor eliminado'
        ], 200);
    }
}
