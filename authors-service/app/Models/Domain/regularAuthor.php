<?php
namespace App\Models\Domain;

/**
 * Clase derivada: Especializa BaseAuthor para autores regulares.
 * Dónde: en authors-service, añade afiliación y fecha de registro.
 * Por qué: permite comportamiento/atributos específicos manteniendo
 * el contrato del dominio (getRoleDescription).
 */
class RegularAuthor extends BaseAuthor {
    private $affiliation;
    private $registrationDate;

    public function __construct($idAuthor, $name, $email, $bio, $affiliation, $registrationDate) {
        parent::__construct($idAuthor, $name, $email, $bio);
        $this->affiliation = $affiliation;
        $this->registrationDate = $registrationDate;
    }

    public function getRoleDescription(): string {
        return "Autor estándar de la editorial";
    }

    public function getAffiliation(): string { return $this->affiliation; }
}
