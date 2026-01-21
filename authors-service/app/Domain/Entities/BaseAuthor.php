<?php
namespace App\Domain\Entities;

abstract class BaseAuthor {
    protected $name;
    protected $email;

    abstract public function getRole(): string;
}
