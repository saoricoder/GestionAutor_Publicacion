<?php
namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class storeAuthorRequest extends FormRequest {
    public function authorize(): bool
    {
        return true;
    }
    
    public function rules(): array {
        $id = $this->route('id');
        return [
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:authors,email,' . $id . ',idAuthor',
            'bio' => 'nullable|string',
            'affiliation' => 'nullable|string'
        ];
    }
}
