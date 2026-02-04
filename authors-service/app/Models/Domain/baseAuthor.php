<?php
namespace App\Models\Domain;

/**
 * Clase abstracta (Template): Base para autores del dominio.
 * Dónde: en authors-service para unificar atributos (id, nombre, email, bio).
 * Por qué: estandariza acceso y obliga a definir comportamiento específico
 * (getRoleDescription) en derivadas.
 */
abstract class BaseAuthor {
    protected $idAuthor;
    protected $name;
    protected $email;
    protected $bio;

    public function __construct($idAuthor, $name, $email, $bio) {
        $this->idAuthor = $idAuthor;
        $this->name = $name;
        $this->email = $email;
        $this->bio = $bio;
    }

    public function getId() { return $this->idAuthor; }
    public function getName() { return $this->name; }
    public function getEmail() { return $this->email; }
    public function getBio() { return $this->bio; }

    abstract public function getRoleDescription(): string;
}
