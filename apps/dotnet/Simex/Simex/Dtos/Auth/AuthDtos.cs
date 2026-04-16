namespace Simex.Dtos.Auth;

public class LoginRequestDto
{
    public string Username { get; set; } = string.Empty;

    public string Password { get; set; } = string.Empty;
}

public class AuthResponseDto
{
    public string AccessToken { get; set; } = string.Empty;

    public string TokenType { get; set; } = "Bearer";

    public UserDto User { get; set; } = null!;
}

public class UserDto
{
    public int Id { get; set; }

    public string Email { get; set; } = string.Empty;

    public string Nombre { get; set; } = string.Empty;

    public RoleDto Rol { get; set; } = null!;

    public CompanyDto? Company { get; set; }

    public string Username { get; set; } = string.Empty;

    public bool Active { get; set; }

    public string? IdentificationCardPath { get; set; }
}

public class RoleDto
{
    public int Id { get; set; }

    public string Name { get; set; } = string.Empty;
}

public class CompanyDto
{
    public int Id { get; set; }

    public string Name { get; set; } = string.Empty;
}

public class UpdateIdentificationCardPathDto
{
    public string IdentificationCardPath { get; set; } = string.Empty;
}
