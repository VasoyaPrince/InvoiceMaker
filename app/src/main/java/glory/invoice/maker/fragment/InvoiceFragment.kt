package glory.invoice.maker.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import glory.invoice.maker.adapter.InvoiceAdapter
import glory.invoice.maker.databinding.FragmentInvoiceBinding
import glory.invoice.maker.viewmodel.InvoiceViewModel


class InvoiceFragment : Fragment() {
    lateinit var binding: FragmentInvoiceBinding
    private lateinit var activityContext: Context
    private val invoiceViewModel: InvoiceViewModel by lazy {
        ViewModelProvider(this)[InvoiceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        activityContext = requireActivity()


        return binding.root
    }

}