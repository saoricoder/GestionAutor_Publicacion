<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\authorController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
| Aquí es donde registras las rutas para tu microservicio.
*/

// Agrupamos las rutas de autores para mantener el código limpio
Route::prefix('authors')->group(function () {

    // Listar todos los autores 
    Route::get('/', [authorController::class, 'index']);

    // Obtener un autor específico por ID 
    Route::get('/{id}', [authorController::class, 'show']);

    // Crear un nuevo autor
    Route::post('/', [authorController::class, 'store']);

    Route::put('/{id}', [authorController::class, 'update']);
    Route::delete('/{id}', [authorController::class, 'destroy']);
});
