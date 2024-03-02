package com.nautsch.htmxbook.contactmanagement

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/contacts")
class ContactsController(
    private val contactRepository: ContactRepository
) {
    @GetMapping("")
    fun contacts(model: ModelMap): ModelAndView {
        model.addAttribute("contacts", contactRepository.fetchAll())
        return ModelAndView("index", model)
    }
    @GetMapping("/{id}")
    fun getContact(@PathVariable id: String, model: ModelMap): ModelAndView {
        val contact = contactRepository.findById(UUID.fromString(id))
        model.addAttribute("contact", contact)
        return ModelAndView("show", model)
    }

    @GetMapping("/new")
    fun contacts_new(model: ModelMap): ModelAndView {
        model.addAttribute("newContact", NewContactForm())
        return ModelAndView("new", model)
    }

    @GetMapping("/{id}/edit")
    fun editContact(@PathVariable id: String, model: ModelMap): ModelAndView {
        val contact = contactRepository.findById(UUID.fromString(id))
        model.addAttribute("contact", contact)
        return ModelAndView("edit", model)
    }

    @PostMapping("/new")
    fun handleNewContact(
        @ModelAttribute("newContact") newContact: NewContactForm,
        model: ModelMap,
        redirectAttributes: RedirectAttributes,
        ): String {

        contactRepository.save(ContactUnsaved(
            name = newContact.name,
            email = newContact.email,
            phone = newContact.phone
        ))

        redirectAttributes.addFlashAttribute("message", "Contact created")

        return "redirect:/contacts"
    }
    @PostMapping("/{id}/edit")
    fun handleEditContact(
        @ModelAttribute("contact") editContact: EditContactForm,
        model: ModelMap,
        redirectAttributes: RedirectAttributes,
    ): String {

        contactRepository.save(Contact(
            id = UUID.fromString(editContact.id),
            name = editContact.name,
            email = editContact.email,
            phone = editContact.phone
        ))

        redirectAttributes.addFlashAttribute("message", "Contact saved")

        return "redirect:/contacts/${editContact.id}"
    }
    @PostMapping("/{id}/delete")
    fun handleDeleteContact(
        @PathVariable id: String,
        model: ModelMap,
        redirectAttributes: RedirectAttributes,
    ): String {
        contactRepository.delete(UUID.fromString(id))
        redirectAttributes.addFlashAttribute("message", "Contact deleted")
        return "redirect:/contacts"
    }
}

class NewContactForm {
    var name: String = ""
    var email: String = ""
    var phone: String = ""
}
class EditContactForm {
    var id: String = ""
    var name: String = ""
    var email: String = ""
    var phone: String = ""
}