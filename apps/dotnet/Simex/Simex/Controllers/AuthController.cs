using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Simex.Dtos.Auth;
using Simex.Models;

namespace Simex.Controllers;

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly Simex04Context _context;
    private readonly IConfiguration _configuration;

    public AuthController(Simex04Context context, IConfiguration configuration)
    {
        _context = context;
        _configuration = configuration;
    }

    [AllowAnonymous]
    [HttpPost("login")]
    public async Task<ActionResult<AuthResponseDto>> Login([FromBody] LoginRequestDto request)
    {
        if (string.IsNullOrWhiteSpace(request.Username) || string.IsNullOrWhiteSpace(request.Password))
        {
            return BadRequest(new { message = "Username y password son obligatorios." });
        }

        var user = await _context.Users
            .Include(u => u.Role)
            .Include(u => u.Company)
            .SingleOrDefaultAsync(u => u.Username == request.Username);

        if (user is null || !VerifyPassword(request.Password, user.Password))
        {
            return Unauthorized(new { message = "Credenciales invalidas." });
        }

        if (user.Active != true)
        {
            return Unauthorized(new { message = "La cuenta esta desactivada." });
        }

        var token = CreateToken(user);

        return Ok(new AuthResponseDto
        {
            AccessToken = token,
            User = MapUser(user)
        });
    }

    [Authorize]
    [HttpGet("me")]
    public async Task<ActionResult<UserDto>> Me()
    {
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdClaim, out var userId))
        {
            return Unauthorized(new { message = "Token invalido." });
        }

        var user = await _context.Users
            .Include(u => u.Role)
            .Include(u => u.Company)
            .SingleOrDefaultAsync(u => u.Id == userId);

        if (user is null)
        {
            return NotFound(new { message = "Usuario no encontrado." });
        }

        return Ok(MapUser(user));
    }

    private static bool VerifyPassword(string plainPassword, string hashedPassword)
    {
        if (string.IsNullOrWhiteSpace(hashedPassword))
        {
            return false;
        }

        var normalizedHash = hashedPassword.StartsWith("$2y$")
            ? "$2a$" + hashedPassword[4..]
            : hashedPassword;

        return BCrypt.Net.BCrypt.Verify(plainPassword, normalizedHash);
    }

    private string CreateToken(User user)
    {
        var claims = new List<Claim>
        {
            new(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new(ClaimTypes.Name, user.Username ?? user.Email),
            new(ClaimTypes.Email, user.Email)
        };

        if (!string.IsNullOrWhiteSpace(user.Role?.Name))
        {
            claims.Add(new Claim(ClaimTypes.Role, user.Role.Name));
        }

        var key = new SymmetricSecurityKey(
            Encoding.UTF8.GetBytes(_configuration["Jwt:Key"] ?? ""));
        var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        var token = new JwtSecurityToken(
            issuer: _configuration["Jwt:Issuer"],
            audience: _configuration["Jwt:Audience"],
            claims: claims,
            expires: DateTime.UtcNow.AddMinutes(120),
            signingCredentials: credentials);

        return new JwtSecurityTokenHandler().WriteToken(token);
    }

    private static UserDto MapUser(User user)
    {
        return new UserDto
        {
            Id = user.Id,
            Email = user.Email,
            Nombre = $"{user.FirstName} {user.LastName}".Trim(),
            Rol = new RoleDto
            {
                Id = user.Role.Id,
                Name = user.Role.Name
            },
            Company = user.Company is null
                ? null
                : new CompanyDto
                {
                    Id = user.Company.Id,
                    Name = user.Company.Name
                },
            Username = user.Username ?? string.Empty,
            Active = user.Active ?? false,
            IdentificationCardPath = user.IdentificationCardPath
        };
    }
}
