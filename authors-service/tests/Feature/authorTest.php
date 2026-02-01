<?php

namespace Tests\Feature;

use App\Models\Entities\author;
use database\factories\AuthorFactory;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

/**
 * AuthorTest
 *
 * Suite de pruebas para validar el funcionamiento del servicio de autores
 * Incluye: validación de API, manejo de errores, casos de éxito
 */
class AuthorTest extends TestCase
{
    use RefreshDatabase; // Limpia BD después de cada test

    /**
     * TEST: Obtener lista de autores
     * Verifica que GET /api/authors devuelva 200 con lista de autores
     */
    public function test_can_get_all_authors()
    {
        // Arrange: Crea 5 autores en BD
        author::factory(5)->create();

        // Act: Realiza solicitud HTTP
        $response = $this->getJson('/api/authors');

        // Assert: Valida respuesta
        $response->assertStatus(200)
                 ->assertJsonStructure([
                     'success',
                     'message',
                     'data' => [
                         '*' => ['id', 'name', 'email', 'bio']
                     ],
                     'count'
                 ])
                 ->assertJsonPath('success', true)
                 ->assertJsonPath('count', 5);
    }

    /**
     * TEST: Obtener autor por ID
     * Verifica que GET /api/authors/{id} retorne el autor correcto
     */
    public function test_can_get_author_by_id()
    {
        // Arrange
        $author = Author::factory()->create();

        // Act
        $response = $this->getJson("/api/authors/{$author->id}");

        // Assert
        $response->assertStatus(200)
                 ->assertJsonPath('success', true)
                 ->assertJsonPath('data.id', $author->id)
                 ->assertJsonPath('data.name', $author->name)
                 ->assertJsonPath('data.email', $author->email);
    }

    /**
     * TEST: Obtener autor inexistente
     * Verifica que devuelva 404 cuando el autor no existe
     */
    public function test_get_nonexistent_author_returns_404()
    {
        // Act
        $response = $this->getJson('/api/authors/999999');

        // Assert
        $response->assertStatus(404)
                 ->assertJsonPath('success', false)
                 ->assertJsonPath('message', 'Autor no encontrado');
    }

    /**
     * TEST: Crear autor con datos válidos
     * Verifica que POST /api/authors cree un nuevo autor
     */
    public function test_can_create_author_with_valid_data()
    {
        // Arrange
        $authorData = [
            'name' => 'John Doe',
            'email' => 'john@example.com',
            'bio' => 'A talented author'
        ];

        // Act
        $response = $this->postJson('/api/authors', $authorData);

        // Assert
        $response->assertStatus(201)
                 ->assertJsonPath('success', true)
                 ->assertJsonPath('data.name', 'John Doe')
                 ->assertJsonPath('data.email', 'john@example.com');

        // Verifica que el autor fue insertado en BD
        $this->assertDatabaseHas('authors', [
            'email' => 'john@example.com'
        ]);
    }

    /**
     * TEST: Crear autor sin nombre
     * Verifica validación de campos requeridos
     */
    public function test_cannot_create_author_without_name()
    {
        // Arrange
        $authorData = [
            'email' => 'john@example.com',
            'bio' => 'A bio'
        ];

        // Act
        $response = $this->postJson('/api/authors', $authorData);

        // Assert
        $response->assertStatus(422)
                 ->assertJsonPath('success', false)
                 ->assertJsonValidationErrors(['name']);
    }

    /**
     * TEST: Crear autor con email inválido
     * Verifica validación de formato email
     */
    public function test_cannot_create_author_with_invalid_email()
    {
        // Arrange
        $authorData = [
            'name' => 'John Doe',
            'email' => 'invalid-email',
            'bio' => 'A bio'
        ];

        // Act
        $response = $this->postJson('/api/authors', $authorData);

        // Assert
        $response->assertStatus(422)
                 ->assertJsonValidationErrors(['email']);
    }

    /**
     * TEST: Crear autor con email duplicado
     * Verifica que no permita emails duplicados
     */
    public function test_cannot_create_author_with_duplicate_email()
    {
        // Arrange
        $existingAuthor = Author::factory()->create([
            'email' => 'duplicate@example.com'
        ]);

        $newAuthorData = [
            'name' => 'Another Author',
            'email' => 'duplicate@example.com',
            'bio' => 'Different bio'
        ];

        // Act
        $response = $this->postJson('/api/authors', $newAuthorData);

        // Assert
        $response->assertStatus(422)
                 ->assertJsonValidationErrors(['email']);
    }

    /**
     * TEST: Actualizar autor
     * Verifica que PUT /api/authors/{id} actualice correctamente
     */
    public function test_can_update_author()
    {
        // Arrange
        $author = Author::factory()->create();
        $updateData = [
            'name' => 'Updated Name',
            'bio' => 'Updated bio'
        ];

        // Act
        $response = $this->putJson("/api/authors/{$author->id}", $updateData);

        // Assert
        $response->assertStatus(200)
                 ->assertJsonPath('success', true)
                 ->assertJsonPath('data.name', 'Updated Name');

        $this->assertDatabaseHas('authors', [
            'id' => $author->id,
            'name' => 'Updated Name'
        ]);
    }

    /**
     * TEST: Actualizar autor no existente
     * Verifica que devuelva 404
     */
    public function test_cannot_update_nonexistent_author()
    {
        // Act
        $response = $this->putJson('/api/authors/999999', [
            'name' => 'New Name'
        ]);

        // Assert
        $response->assertStatus(404)
                 ->assertJsonPath('success', false);
    }

    /**
     * TEST: Eliminar autor
     * Verifica que DELETE /api/authors/{id} elimine el autor
     */
    public function test_can_delete_author()
    {
        // Arrange
        $author = Author::factory()->create();

        // Act
        $response = $this->deleteJson("/api/authors/{$author->id}");

        // Assert
        $response->assertStatus(200)
                 ->assertJsonPath('success', true);

        $this->assertDatabaseMissing('authors', [
            'id' => $author->id
        ]);
    }

    /**
     * TEST: Eliminar autor no existente
     * Verifica que devuelva 404
     */
    public function test_cannot_delete_nonexistent_author()
    {
        // Act
        $response = $this->deleteJson('/api/authors/999999');

        // Assert
        $response->assertStatus(404)
                 ->assertJsonPath('success', false);
    }

    /**
     * TEST: Crear autor con nombre muy corto
     * Verifica validación de longitud mínima
     */
    public function test_cannot_create_author_with_short_name()
    {
        // Arrange
        $authorData = [
            'name' => 'JD', // Solo 2 caracteres
            'email' => 'john@example.com'
        ];

        // Act
        $response = $this->postJson('/api/authors', $authorData);

        // Assert
        $response->assertStatus(422)
                 ->assertJsonValidationErrors(['name']);
    }
}
