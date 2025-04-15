select ur.users_roles_id, rt.role_type_id from users_roles ur
left join role_types rt on rt.role_type_id = ur.role_type_id
where user_id = ?
