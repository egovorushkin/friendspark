import com.friendspark.backend.entity.User
import com.friendspark.backend.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User): Map<String, Any> {
        return mapOf(
            "id" to user.id,
            "email" to user.email,
            "name" to user.name
        )
    }
}