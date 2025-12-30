package com.example.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AppUser;
import com.example.demo.model.Memo;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.MemoRepository;

@Controller
@RequestMapping("/memos")
public class MemoController {

    private final MemoRepository memoRepo;
    private final AppUserRepository userRepo;

    public MemoController(MemoRepository memoRepo, AppUserRepository userRepo) {
        this.memoRepo = memoRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String memos(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        AppUser user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Logged-in user not found in DB"));

        model.addAttribute(
                "memos",
                memoRepo.findByUserOrderByPinnedDescIdDesc(user)
        );

        return "memos";
    }

    @PostMapping("/create")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         Principal principal) {

        AppUser user = userRepo.findByUsername(principal.getName())
                .orElseThrow();

        memoRepo.save(new Memo(title, content, user));
        return "redirect:/memos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {

        Memo memo = memoRepo.findById(id).orElseThrow();

        if (memo.getUser().getUsername().equals(principal.getName())) {
            memoRepo.delete(memo);
        }

        return "redirect:/memos";
    }
    
    @PostMapping("/pin/{id}")
    public String pin(@PathVariable Long id, Principal principal) {

        Memo memo = memoRepo.findById(id).orElseThrow();

        if (memo.getUser().getUsername().equals(principal.getName())) {
            memo.setPinned(!memo.isPinned());
            memoRepo.save(memo);
        }

        return "redirect:/memos";
    }

}
