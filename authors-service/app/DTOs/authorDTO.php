<?php

namespace App\DTOs;

use Illuminate\Support\Str;

class authorDTO
{
    public function __construct(
        public ?string $idAuthor,
        public string $name,
        public string $email,
        public ?string $bio,
        public string $roleDescription,
        public ?string $affiliation
    ) {}

    public static function fromDomain($domainAuthor, $affiliation = null): self
    {
        return new self(
            $domainAuthor->getId(),
            $domainAuthor->getName(),
            $domainAuthor->getEmail(),
            $domainAuthor->getBio(),
            $domainAuthor->getRoleDescription(),
            $affiliation
        );
    }
}
