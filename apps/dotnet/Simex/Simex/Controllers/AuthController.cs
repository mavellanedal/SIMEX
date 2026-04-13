using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Simex.Dtos.Auth;
using Simex.Models;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

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
        ActionResult<AuthResponseDto> result;
        if (string.IsNullOrWhiteSpace(request.Username) || string.IsNullOrWhiteSpace(request.Password))
        {
            result = BadRequest(new { message = "Username y password son obligatorios." });
        }
        else
        {
            var user = await _context.Users
                .Include(u => u.Role)
                .Include(u => u.Company)
                .Where(u => u.Username == request.Username)
                .SingleOrDefaultAsync();

            if (user == null)
            {
                result = Unauthorized(new { message = "Credenciales invalidas." });
            }
            else
            {
                if (!VerifyPassword(request.Password, user.Password))
                {
                    result = Unauthorized(new { message = "Credenciales invalidas." });
                }
                else
                {
                    if (user.Active != true)
                    {
                        result = Unauthorized(new { message = "La cuenta esta desactivada." });
                    }
                    else
                    {
                        var token = CreateToken(user);

                        result = Ok(new AuthResponseDto
                        {
                            AccessToken = token,
                            User = MapUser(user)
                        });
                    }
                }
            }
        }

        return result;
    }

    [Authorize]
    [HttpGet("me")]
    public async Task<ActionResult<UserDto>> Me()
    {
        ActionResult<UserDto> result;
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);

        if (!int.TryParse(userIdClaim, out var userId))
        {
            result = Unauthorized(new { message = "Token invalido." });
        }
        else
        {
            var user = await _context.Users
                .Include(u => u.Role)
                .Include(u => u.Company)
                .Where(u => u.Id == userId)
                .SingleOrDefaultAsync();

            if (user == null)
            {
                result = NotFound(new { message = "Usuario no encontrado." });
            }
            else
            {
                result = Ok(MapUser(user));
            }
        }

        return result;
    }

    private static bool VerifyPassword(string plainPassword, string hashedPassword)
    {
        bool result;
        string normalizedHash;

        if (string.IsNullOrWhiteSpace(hashedPassword))
        {
            result = false;
        }
        else
        {
            if (hashedPassword.StartsWith("$2y$"))
            {
                normalizedHash = "$2a$" + hashedPassword.Substring(4);
            }
            else
            {
                normalizedHash = hashedPassword;
            }

            result = BCrypt.Net.BCrypt.Verify(plainPassword, normalizedHash);
        }

        return result;
    }

    private string CreateToken(User user)
    {
        string tokenString;
        List<Claim> claims = new List<Claim>();
        SymmetricSecurityKey key;
        SigningCredentials credentials;
        JwtSecurityToken token;

        claims.Add(new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()));
        claims.Add(new Claim(ClaimTypes.Name, user.Username ?? user.Email));
        claims.Add(new Claim(ClaimTypes.Email, user.Email));

        if (user.Role != null)
        {
            if (!string.IsNullOrWhiteSpace(user.Role.Name))
            {
                claims.Add(new Claim(ClaimTypes.Role, user.Role.Name));
            }
        }

        key = new SymmetricSecurityKey(
            Encoding.UTF8.GetBytes(_configuration["Jwt:Key"] ?? "")
        );

        credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        token = new JwtSecurityToken(
            issuer: _configuration["Jwt:Issuer"],
            audience: _configuration["Jwt:Audience"],
            claims: claims,
            expires: DateTime.UtcNow.AddMinutes(120),
            signingCredentials: credentials
        );

        tokenString = new JwtSecurityTokenHandler().WriteToken(token);

        return tokenString;
    }

    private static UserDto MapUser(User user)
    {
        UserDto result;
        RoleDto role;
        CompanyDto company;

        if (user.Role == null)
        {
            role = null;
        }
        else
        {
            role = new RoleDto
            {
                Id = user.Role.Id,
                Name = user.Role.Name
            };
        }

        if (user.Company == null)
        {
            company = null;
        }
        else
        {
            company = new CompanyDto
            {
                Id = user.Company.Id,
                Name = user.Company.Name
            };
        }

        result = new UserDto
        {
            Id = user.Id,
            Email = user.Email,
            Nombre = $"{user.FirstName} {user.LastName}".Trim(),
            Rol = role,
            Company = company,
            Username = user.Username ?? string.Empty,
            Active = user.Active ?? false,
            IdentificationCardPath = user.IdentificationCardPath
        };

        return result;
    }
}
