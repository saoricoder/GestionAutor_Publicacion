<?php
use App\Http\Controllers\Api\AuthorController;
use Illuminate\Support\Facades\Route;

Route::get('/authors', [AuthorController::class, 'index']); [cite: 31]
Route::get('/authors/{id}', [AuthorController::class, 'show']); [cite: 30]
Route::post('/authors', [AuthorController::class, 'store']); [cite: 29]
