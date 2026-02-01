<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthorController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
| Aquí es donde registras las rutas para tu microservicio.
*/

// Agrupamos las rutas de autores para mantener el código limpio
Route::prefix('authors')->group(function () {

    // Listar todos los autores
    Route::get('/', [AuthorController::class, 'index']);

    // Obtener un autor específico por ID
    Route::get('/{id}', [AuthorController::class, 'show']);

    // Crear un nuevo autor
    Route::post('/', [AuthorController::class, 'store']);

    Route::put('/{id}', [AuthorController::class, 'update']);
    Route::delete('/{id}', [uthorController::class, 'destroy']);
});
