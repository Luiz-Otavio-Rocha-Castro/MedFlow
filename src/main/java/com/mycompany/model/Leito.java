package com.mycompany.model;

public class Leito {
    private Integer id;
    private String leito;
    private TipoLeito tipoLeito;
    private boolean disponivel;
    private Paciente pacienteAlocado;


    public Leito(String leito, TipoLeito tipoLeito) {
        this.id = 0;
        this.leito = leito;
        this.tipoLeito = tipoLeito;
        this.disponivel = true;
        this.pacienteAlocado = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeito() {
        return leito;
    }

    public void setLeito(String leito) {
        this.leito = leito;
    }

    public TipoLeito getTipoLeito() {
        return tipoLeito;
    }

    public void setTipoLeito(TipoLeito tipoLeito) {
        this.tipoLeito = tipoLeito;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Paciente getPacienteAlocado() {
        return pacienteAlocado;
    }

    public void setPacienteAlocado(Paciente pacienteAlocado) {
        this.pacienteAlocado = pacienteAlocado;
    }
}
