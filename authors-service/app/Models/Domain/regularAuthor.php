<?php
namespace App\Models\Domain;

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
