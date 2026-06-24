package com.spamdetection.controller;

import com.spamdetection.dto.Dtos.*;
import com.spamdetection.model.Email;
import com.spamdetection.model.User;
import com.spamdetection.service.EmailService;
import com.spamdetection.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * MAIN CONTROLLER
 * Replaces Django's views.py + urls.py combined
 *
 * Django view:             →  Java equivalent:
 * def home(request):       →  @GetMapping("/home")
 * def check(request):      →  @PostMapping("/check")
 * def dashboard(request):  →  @GetMapping("/dashboard")
 * @login_required          →  Handled by SecurityConfig (no decorator needed)
 */
@Controller
@RequiredArgsConstructor
public class SpamController {

    private final EmailService emailService;
    private final UserService userService;

    // ============================================================
    // HOME PAGE — GET /home
    // Replaces Django: def home(request): return render(request, 'home.html')
    // ============================================================
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("emailCheckRequest", new EmailCheckRequest());
        return "home";  // → src/main/resources/templates/home.html
    }

    // ============================================================
    // CHECK EMAIL — POST /check
    // Replaces Django: def check_email(request): if request.method == 'POST'
    // ============================================================
    @PostMapping("/check")
    public String checkEmail(@Valid @ModelAttribute EmailCheckRequest request,
                             BindingResult bindingResult,
                             Authentication auth,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "home";
        }

        // Get the currently logged-in user
        User user = userService.getUserByUsername(auth.getName());

        // Call EmailService → MlService → Python Flask → returns prediction
        Email result = emailService.checkEmail(request.getContent(), user);

        // Pass result to template (like Django's context dict)
        model.addAttribute("result", result);
        model.addAttribute("emailCheckRequest", new EmailCheckRequest());
        model.addAttribute("isSpam", "spam".equals(result.getPrediction()));

        return "home";  // stay on home page and show result
    }

    // ============================================================
    // DASHBOARD — GET /dashboard
    // Replaces Django: def dashboard(request)
    // ============================================================
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        List<Email> emails = emailService.getUserEmails(user);
        DashboardStats stats = emailService.getStats(user);

        model.addAttribute("emails", emails);
        model.addAttribute("stats", stats);
        model.addAttribute("user", user);

        return "dashboard";  // → templates/dashboard.html
    }

    // ============================================================
    // DELETE EMAIL RECORD — POST /delete/{id}
    // ============================================================
    @PostMapping("/delete/{id}")
    public String deleteEmail(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        emailService.deleteEmail(id);
        redirectAttributes.addFlashAttribute("message", "Record deleted successfully");
        return "redirect:/dashboard";
    }

    // ============================================================
    // REGISTER — GET /register (show form)
    // Replaces Django: def register(request): return render(request, 'register.html')
    // ============================================================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";  // → templates/register.html
    }

    // ============================================================
    // REGISTER — POST /register (process form)
    // Replaces Django: UserCreationForm + form.save()
    // ============================================================
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("success",
                "Account created! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ============================================================
    // LOGIN PAGE — GET /login
    // Spring Security handles POST /login automatically
    // ============================================================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // → templates/login.html
    }
}
